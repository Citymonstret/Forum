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
@Entity("categories")
@EqualsAndHashCode
@NoArgsConstructor( onConstructor=@__({@KvantumConstructor}))
@AllArgsConstructor
@Getter
@Builder
public class Category
{

    @Id
    @Min( -1 )
    @KvantumField
    private int categoryId;

    @NotEmpty
    @KvantumField
    private String categoryName;

    @NotEmpty
    @KvantumField
    private String categoryDescription;

}
