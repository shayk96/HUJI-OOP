/**
 * a factory class for the different renderer types
 */
public class RendererFactory {

    /**
     * creates the renderer type desired
     * @param rendererType the renderer type desired
     * @return the renderer type else null
     */
    public Renderer buildRenderer(String rendererType) {
        switch (rendererType) {
            case "console":
                return new ConsoleRenderer();
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
