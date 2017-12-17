package xyz.kvantum.forum.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import xyz.kvantum.server.api.orm.annotations.KvantumConstructor;
import xyz.kvantum.server.api.orm.annotations.KvantumField;
import xyz.kvantum.server.api.orm.annotations.KvantumObject;

@KvantumObject(checkValidity = true)
@Entity("topics")
@EqualsAndHashCode
@NoArgsConstructor( onConstructor=@__({@KvantumConstructor }))
@AllArgsConstructor
@Getter
@Builder
public class Topic
{

    @Id
    @Min(-1)
    @KvantumField
    private int topicId;

    @NotEmpty
    @KvantumField
    private String topicSubject;

    @Min( 0 )
    @KvantumField
    private long topicDate;

    @Min( -1 )
    @KvantumField
    private int categoryId;

    @Min( -1 )
    @KvantumField
    private int topicAuthorId;

}



