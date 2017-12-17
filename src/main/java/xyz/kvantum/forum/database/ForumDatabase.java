package xyz.kvantum.forum.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import lombok.Synchronized;
import xyz.kvantum.server.api.config.CoreConfig;
import xyz.kvantum.server.api.core.ServerImplementation;
import xyz.kvantum.server.implementation.MongoApplicationStructure;

public final class ForumDatabase
{

    private static final String COUNTER_TOPIC = "topicId";
    private static final String COUNTER_REPLY = "replyId";
    private static final String COUNTER_CATEGORY = "categoryId";

    private final MongoApplicationStructure mongoApplicationStructure;
    private final DBCollection counters;

    public ForumDatabase()
    {
        this.mongoApplicationStructure = ( MongoApplicationStructure ) ServerImplementation.getImplementation()
                .getApplicationStructure();

        //
        // Setup counter collection
        //
        final DB database = this.mongoApplicationStructure.getMongoClient().getDB( CoreConfig.MongoDB.dbMorphia );
        this.counters = database.getCollection( "counters" );

        //
        // Create counters
        //
        this.createCounterIfNotExists( COUNTER_TOPIC );
        this.createCounterIfNotExists( COUNTER_REPLY );
        this.createCounterIfNotExists( COUNTER_CATEGORY );
    }

    private void createCounterIfNotExists(final String counterId)
    {
        if ( !this.counters.find( new BasicDBObject( "_id", counterId ) ).hasNext() )
        {
            this.counters.insert( new BasicDBObject( "_id", counterId ).append( "seq", 0 ) );
        }
    }

    @Synchronized
    private int getNextId(final String counterId)
    {
        return (int) counters.findAndModify( new BasicDBObject( "_id", counterId ),
                new BasicDBObject( "$inc", new BasicDBObject( "seq", 1 ) ) ).get( "seq" );
    }

    public int getNextTopicId()
    {
        return this.getNextId( COUNTER_TOPIC );
    }

    public int getNextCategoryId()
    {
        return this.getNextId( COUNTER_CATEGORY );
    }

    public int getNextReplyId()
    {
        return this.getNextId( COUNTER_REPLY );
    }

}
