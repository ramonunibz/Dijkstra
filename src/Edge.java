public class Edge {
    public String fromNode;
    public String toNode;
    public int cost;

    public Edge(String fromNode, String toNode, int cost){
        this.fromNode=fromNode;
        this.toNode=toNode;
        this.cost=cost;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "fromNode='" + fromNode + '\'' +
                ", toNode='" + toNode + '\'' +
                ", cost=" + cost +
                '}';
    }
}
