package xyz.kvantum.forum.views;

import lombok.NonNull;
import lombok.SneakyThrows;
import xyz.kvantum.forum.files.ForumFileSystem;
import xyz.kvantum.server.api.core.ServerImplementation;
import xyz.kvantum.server.api.request.AbstractRequest;
import xyz.kvantum.server.api.response.Response;
import xyz.kvantum.server.api.views.staticviews.StaticViewManager;
import xyz.kvantum.server.api.views.staticviews.ViewMatcher;
import xyz.kvantum.server.api.views.staticviews.converters.StandardConverters;

public class IndexView
{
    private final ForumFileSystem forumFileSystem;

    @SneakyThrows
    public IndexView(@NonNull final ForumFileSystem forumFileSystem)
    {
        this.forumFileSystem = forumFileSystem;
        StaticViewManager.generate( this );
    }

    @SuppressWarnings( "ALL" )
    @ViewMatcher( filter = "", outputType = StandardConverters.HTML )
    public String index(final AbstractRequest request)
    {
        final StringBuilder document = new StringBuilder();
        document.append( forumFileSystem.getTemplate( "header.html" ).readFile() );
        document.append( forumFileSystem.getTemplate( "index.html" ).readFile() );
        document.append( forumFileSystem.getTemplate( "footer.html" ).readFile() );
        return document.toString();
    }

    @SuppressWarnings( "ALL" )
    @ViewMatcher( filter = "login" )
    public Response login(final AbstractRequest request)
    {
        if ( ServerImplementation.getImplementation().getApplicationStructure().getAccountManager().getAccount(
                request.getSession() ).isPresent() )
        {
            final Response response = new Response();
            response.setContent( "<meta http-equiv=\"refresh\" content=\"1; URL='/'\" />" +
                    "You are already logged in..." );
            return response;
        }
        final StringBuilder document = new StringBuilder();
        document.append( forumFileSystem.getTemplate( "header.html" ).readFile() );
        document.append( forumFileSystem.getTemplate( "login.html" ).readFile() );
        final Response response = new Response();
        response.setContent( document.toString() );
        return response;
    }

}
