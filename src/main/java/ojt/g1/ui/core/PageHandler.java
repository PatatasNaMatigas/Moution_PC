package ojt.g1.ui.core;

import java.util.ArrayList;

public final class PageHandler {

    private final ArrayList<Page> pages = new ArrayList<>();

    private int currentPageId = 0;

    private Window window;

    public void addPage(Page page) {
        pages.add(page);
        page.setId(pages.size() - 1);
    }

    public Page getPage(String name) {
        for (Page page : pages)
            if (page.getName().equals(name))
                return page;
        return null;
    }

    public void setPage(String name) {
        for (Page page : pages) {
            if (page.getName().equals(name)) {
                currentPageId = page.getId();
                return;
            }
        }
    }

    public Page getCurrentPage() {
        return pages.get(currentPageId);
    }
}
