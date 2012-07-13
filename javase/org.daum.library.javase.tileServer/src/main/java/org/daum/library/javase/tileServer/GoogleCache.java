package org.daum.library.javase.tileServer;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 12/07/12
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */

public class GoogleCache extends TileCacheImpl {

    private int serverIndex = 0;

    public GoogleCache(String pathCache) {
        super(pathCache);
    }

    /**
     * Parse request and create a new tile with x, y, and zoom level
     * @param path uri request path
     * @return a new Tile from request query string
     */
    protected Tile parseTile(String path) {
        Tile tile = null;
        // todo


        return tile;
    }

    @Override
    public String getServerUrl() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getServerUrl(String query) {
        // todo
        String path = "";
        String server = path.equals("vt") ? "mt" : "khm";
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(server);
        sb.append(this.serverIndex++);
        sb.append(".google.com");

        if (serverIndex > 2)
            serverIndex = 0;

        return sb.toString();
    }
}

