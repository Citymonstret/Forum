package xyz.kvantum.forum.files;

import lombok.SneakyThrows;
import xyz.kvantum.files.FileSystem;
import xyz.kvantum.files.Path;
import xyz.kvantum.server.api.core.ServerImplementation;
import xyz.kvantum.server.implementation.FileCacheImplementation;

import java.io.File;

public final class ForumFileSystem extends FileSystem
{

    private static final String PATH_TEMPLATES = "templates";

    public ForumFileSystem()
    {
        super( new File( ServerImplementation.getImplementation().getCoreFolder(), "forum" ).toPath(),
               new FileCacheImplementation() );
        this.createDirectoryIfNotExists( PATH_TEMPLATES );
    }

    @SneakyThrows
    private void createDirectoryIfNotExists(final String path)
    {
        final Path directory = getPath( path );
        if ( !directory.exists() )
        {
            directory.create();
        }
    }

    public Path getTemplatePath()
    {
        return this.getPath( PATH_TEMPLATES );
    }

    public Path getTemplate(final String template)
    {
        return this.getTemplatePath().getPath( template );
    }

}
