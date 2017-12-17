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

public final class LoginHandler
{

    private final IAccountManager accountManager;
    private final RequestRequirements apiRequirements;

    @SneakyThrows
    public LoginHandler(final IAccountManager accountManager)
    {
        this.accountManager = accountManager;
        this.apiRequirements = new RequestRequirements();
        this.apiRequirements.addRequirement( new RequestRequirements.PostVariableRequirement( "username" ) )
                .addRequirement( new RequestRequirements.PostVariableRequirement( "password" ) );
        StaticViewManager.generate( this );
    }

    @ViewMatcher( filter = "internal/login", outputType = StandardConverters.JSON, httpMethod = HttpMethod.POST )
    public JSONObject handleLogin(final AbstractRequest request)
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
            final Optional<IAccount> accountOptional = accountManager.getAccount(
                    username );

            if ( !accountOptional.isPresent() )
            {
                status = ApiStatus.ERROR;
                message = "There is no such account!";
                break eval;
            }

            final IAccount account = accountOptional.get();
            if ( !account.passwordMatches( password ) )
            {
                status = ApiStatus.ERROR;
                message = "Incorrect password!";
                break eval;
            }

            accountManager.bindAccount( account, request.getSession() );
            status = ApiStatus.SUCCESS;
            object.put( "redirect", "/" );
            object.put( "sessionId", request.getSession().getSessionId() );
        }

        object.put( "status", status.getJSONObject() );
        object.put( "message", message );
        return object;
    }

}
