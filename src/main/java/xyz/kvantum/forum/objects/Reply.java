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
@Entity("replies")
@EqualsAndHashCode
@NoArgsConstructor( onConstructor=@__({@KvantumConstructor }))
@AllArgsConstructor
@Getter
@Builder
public class Reply
{

    @Id
    @Min(-1)
    @KvantumField
    private int replyId;

    @Min(-1)
    @KvantumField
    private int topicId;

    @Min(0)
    @KvantumField
    private long replyDate;

    @Min(-1)
    @KvantumField
    private int replyAuthorId;

    @NotEmpty
    @KvantumField
    private String replyContent;

}

