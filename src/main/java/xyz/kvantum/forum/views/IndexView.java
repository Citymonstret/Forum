package xyz.kvantum.forum.views;

import lombok.NonNull;
import lombok.SneakyThrows;
import xyz.kvantum.forum.files.ForumFileSystem;
import xyz.kvantum.server.api.request.AbstractRequest;
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

}
