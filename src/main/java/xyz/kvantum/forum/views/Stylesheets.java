package xyz.kvantum.forum.views;

import com.google.common.base.Charsets;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import xyz.kvantum.files.Path;
import xyz.kvantum.forum.files.ForumFileSystem;
import xyz.kvantum.server.api.request.AbstractRequest;
import xyz.kvantum.server.api.response.Header;
import xyz.kvantum.server.api.response.Response;
import xyz.kvantum.server.api.views.staticviews.StaticViewManager;
import xyz.kvantum.server.api.views.staticviews.ViewMatcher;

@ExtensionMethod( Stylesheets.class )
public class Stylesheets
{

    private final ForumFileSystem forumFileSystem;

    @SneakyThrows
    public Stylesheets(@NonNull final ForumFileSystem forumFileSystem)
    {
        this.forumFileSystem = forumFileSystem;
        StaticViewManager.generate( this );
    }

    private static void prepareResponse(final Response response)
    {
        response.getHeader().set( Header.HEADER_CONTENT_TYPE, Header.CONTENT_TYPE_CSS );
    }

    private byte[] getBytes(final String file)
    {
        final Path path = this.forumFileSystem.getPath( file );
        if ( !path.exists() )
        {
            return new byte[0];
        }
        return path.readFile().getBytes( Charsets.UTF_8 );
    }

    @ViewMatcher( filter = "style.css" )
    public void mainStylesheet(final AbstractRequest request, final Response response)
    {
        prepareResponse( response );
        response.setBytes( getBytes( "style.css" ) );
    }
}
