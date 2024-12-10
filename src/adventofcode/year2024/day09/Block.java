package adventofcode.year2024.day09;

class Block {
    
    private Integer id;

    public Block(Block block) {
        this(block.id);
    }

    public Block() {
        this((Integer) null);
    }

    public Block(Integer id) {
        this.id = id;
    }
    
    public boolean isAllocated() {
        return id != null;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return id == null ? ". " : (String.valueOf(id) + " ");
    }

}
