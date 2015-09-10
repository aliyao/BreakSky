package com.yao.breaksky.fragment.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

   public static void  setData(List<Map<String, Object>>  listData) {

        /*addItem(new DummyItem("1", listData));
        addItem(new DummyItem("2", "Item 2"));
        addItem(new DummyItem("3", "Item 3"));*/
       for (int itemNum=0;itemNum<listData.size();itemNum++){
           addItem(new DummyItem(itemNum+"", listData.get(itemNum).get("title"), listData.get(itemNum).get("url")));

       }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public Object content;
        public Object url;


        public DummyItem(String id, Object content,Object url) {
            this.id = id;
            this.content = content;
            this.url=url;
        }
        @Override
        public String toString() {
            return String.valueOf(content);
        }


    }
}
