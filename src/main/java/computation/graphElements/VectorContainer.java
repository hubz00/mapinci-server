package computation.graphElements;

public class VectorContainer {
    private Long id;
    private Vector vector;

    public VectorContainer(Long id, Vector vector) {
        this.id = id;
        this.vector = vector;
    }

    public VectorContainer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
}
