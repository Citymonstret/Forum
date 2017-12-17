package xyz.kvantum.forum.views;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import xyz.kvantum.server.api.account.IAccount;
import xyz.kvantum.server.api.account.IAccountManager;
import xyz.kvantum.server.api.request.AbstractRequest;
import xyz.kvantum.server.api.request.HttpMethod;
import xyz.kvantum.server.api.request.post.PostRequest;
import xyz.kvantum.server.api.views.rest.ApiStatus;
import xyz.kvantum.server.api.views.rest.RequestRequirements;
import xyz.kvantum.server.api.views.staticviews.StaticViewManager;
import xyz.kvantum.server.api.views.staticviews.ViewMatcher;
import xyz.kvantum.server.api.views.staticviews.converters.StandardConverters;

import java.util.Optional;

public final class RegistrationHandler
{

    private final IAccountManager accountManager;
    private final RequestRequirements apiRequirements;

    @SneakyThrows
    public RegistrationHandler(final IAccountManager accountManager)
    {
        this.accountManager = accountManager;
        this.apiRequirements = new RequestRequirements();
        this.apiRequirements
                .addRequirement( new RequestRequirements.PostVariableRequirement( "username" ) )
                .addRequirement( new RequestRequirements.PostVariableRequirement( "password" ) )
                .addRequirement( new RequestRequirements.PostVariableRequirement( "repeat-password" ) )
                .addRequirement( new RequestRequirements.PostVariableRequirement( "email" ) );
        StaticViewManager.generate( this );
    }

    @ViewMatcher( filter = "internal/register", outputType = StandardConverters.JSON, httpMethod = HttpMethod.POST )
    public JSONObject handleRegistration(final AbstractRequest request)
    {
        final JSONObject object = new JSONObject();
        ApiStatus status;
        String message = "";

        eval:
        {
            if ( accountManager.getAccount( request.getSession() ).isPresent() )
            {
                status = ApiStatus.ERROR;
                message = "Session already bound to account!";
                break eval;
            }
            final PostRequest postRequest = request.getPostRequest();
            if ( postRequest == null )
            {
                status = ApiStatus.ERROR;
                message = "POST request was empty";
                break eval;
            }

            RequestRequirements.RequirementStatus requirementStatus = apiRequirements.testRequirements( request );
            if ( requirementStatus != null && !requirementStatus.passed() )
            {
                status = ApiStatus.ERROR;
                message = requirementStatus.getMessage();
                object.put( "internalMessage", requirementStatus.getInternalMessage() );
                break eval;
            }

            final String username = postRequest.get( "username" );
            final String password = postRequest.get( "password" );
            final String testPassword = postRequest.get( "repeat-password" );

            if ( !password.equals( testPassword ) )
            {
                status = ApiStatus.ERROR;
                message = "Passwords do not match!";
                break eval;
            }

            final Optional<IAccount> accountOptional = accountManager.getAccount(
                    username );

            if ( accountOptional.isPresent() )
            {
                status = ApiStatus.ERROR;
                message = "The username is already taken!";
                break eval;
            }

            final Optional<IAccount> createAccount = accountManager.createAccount( username,
                    password );
            if ( !createAccount.isPresent() )
            {
                status = ApiStatus.ERROR;
                message = "Failed to create account";
                break eval;
            }

            accountManager.bindAccount( createAccount.get(), request.getSession() );
            status = ApiStatus.SUCCESS;
            object.put( "redirect", "/" );
        }

        object.put( "status", status.getJSONObject() );
        object.put( "message", message );
        return object;
    }

}
