package xyz.kvantum.forum;

import lombok.NonNull;
import lombok.SneakyThrows;
import xyz.kvantum.forum.database.ForumDatabase;
import xyz.kvantum.forum.files.ForumFileSystem;
import xyz.kvantum.forum.views.IndexView;
import xyz.kvantum.forum.views.Stylesheets;
import xyz.kvantum.server.api.config.CoreConfig;
import xyz.kvantum.server.api.core.Kvantum;
import xyz.kvantum.server.api.util.RequestManager;
import xyz.kvantum.server.implementation.DefaultLogWrapper;
import xyz.kvantum.server.implementation.ServerContext;
import xyz.kvantum.server.implementation.StandaloneServer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Forum application entry
 */
public final class KvantumForum
{

    @SuppressWarnings( "ALL" )
    private final Kvantum kvantum;
    private final ForumDatabase forumDatabase;
    private final ForumFileSystem forumFileSystem;

    private KvantumForum(@NonNull final Kvantum kvantum)
    {
        this.kvantum = kvantum;
        this.forumDatabase = new ForumDatabase();
        this.forumFileSystem = new ForumFileSystem();
        this.registerHandlers();
    }

    @SneakyThrows
    public static void main(final String[] args)
    {
        //
        // Make sure that Kvantum is configured to use MongoDB
        //
        CoreConfig.Application.databaseImplementation = "mongo";
        CoreConfig.MongoDB.enabled = true;
        CoreConfig.Templates.engine = CoreConfig.TemplatingEngine.CRUSH.name();

        //
        // Retrieve a ServerContext
        //
        final ServerContext serverContext = ServerContext.builder()
                .standalone( true )
                .router( RequestManager.builder().build() )
                .logWrapper( new DefaultLogWrapper() )
                .serverSupplier( StandaloneServer::new )
                .coreFolder( new File( ".", "kvantum" ) )
                .build();
        final Optional<Kvantum> serverOptional = serverContext.create();
        if ( serverOptional.isPresent() )
        {
            {
                final Path path = new File( serverContext.getCoreFolder(), "forum" ).toPath();
                if ( !Files.exists( path ) )
                {
                    Files.createDirectory( path );
                }
            }
            final Kvantum kvantum = serverOptional.get();
            final KvantumForum forum = new KvantumForum( kvantum );
            forum.start();
        } else
        {
            System.out.println( "ERROR: Failed to create server" );
        }
    }

    private void registerHandlers()
    {
        new Stylesheets( this.forumFileSystem );
        new IndexView( this.forumFileSystem );
    }

    @SneakyThrows
    private void start()
    {
        if ( kvantum.isStarted() )
        {
            throw new IllegalAccessException( "Cannot start the server when it's already started..." );
        }
        this.kvantum.start();
    }

}
