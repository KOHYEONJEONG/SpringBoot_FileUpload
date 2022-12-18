package hello.upload.domain;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {//item(상품) 리포지토리

    private  final Map<Long, Item> store = new HashMap<>();//item 저장소
    private  long sequence = 0L; //만약 DB를 사용하면 sequence를 사용하면 된다.

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }
}
