package adventofcode.year2024.day09;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;

public class DayNine {

    public static void main(String[] args) {
//        List<Block> blocks = readInput("tiny_test_input.txt");
        List<Block> blocks = readInput("test_input.txt");
//        List<Block> blocks = readInput("input.txt");

        long part1Start = System.currentTimeMillis();
        
        compressBlocks(blocks.stream().map(Block::new).collect(Collectors.toList()));

        long part2Start = System.currentTimeMillis();
        
        compressBlocksMaintainingFiles(blocks.stream().map(Block::new).collect(Collectors.toList()));

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static void compressBlocks(List<Block> blocks) {
//        System.out.println(blocks.stream().map(Block::toString).collect(Collectors.joining()));
        
        int lastEmptyIndex = -1;
        int lastFullIndex = blocks.size() - 1;
        while (lastFullIndex > lastEmptyIndex) {
            Block nextBlock = blocks.get(lastFullIndex);
            if (!nextBlock.isAllocated()) {
                lastFullIndex--;
                continue;
            }
            
            for (int searchIndex = lastEmptyIndex + 1; searchIndex <= lastFullIndex; searchIndex++) {
                Block searchBlock = blocks.get(searchIndex);
                if (!searchBlock.isAllocated()) {
                    searchBlock.setId(nextBlock.getId());
                    nextBlock.setId(null);
                    lastEmptyIndex = searchIndex;
                    break;
                }
            }
            lastFullIndex--;
            
//            System.out.println(blocks.stream().map(Block::toString).collect(Collectors.joining()));
        }
        
        System.out.println(String.format("File system checksum: %d", calculateChecksum(blocks)));
    }

    private static void compressBlocksMaintainingFiles(List<Block> blocks) {
//        System.out.println(blocks.stream().map(Block::toString).collect(Collectors.joining()));
        
        int lastFullIndex = blocks.size() - 1;
        int lastId = Integer.MAX_VALUE;
        while (lastId > 0) {
            Block nextBlock = blocks.get(lastFullIndex);
            if (!nextBlock.isAllocated() || nextBlock.getId() >= lastId) {
                lastFullIndex--;
                continue;
            }
            
            int nextId = nextBlock.getId();
            
            int fileSize = 1;
            for (int i = lastFullIndex - 1; i > Math.max(lastFullIndex - 9, 0); i--) {
                Block preceedingBlock = blocks.get(i);
                if (!preceedingBlock.isAllocated() || preceedingBlock.getId() != nextBlock.getId()) {
                    break;
                }
                fileSize++;
            }
            
            boolean spaceFound = false;
            int spaceStart = 0;
            int spaceSize = 0;
            for (int searchIndex = 1; searchIndex <= lastFullIndex; searchIndex++) {
                if (blocks.get(searchIndex).isAllocated()) {
                    spaceSize = 0;
                    continue;
                }
                
                if (spaceSize == 0) {
                    spaceStart = searchIndex;
                }
                spaceSize++;
                
                if (spaceSize == fileSize) {
                    spaceFound = true;
                    break;
                }
            }
            
            if (spaceFound) {
                for (int i = 0; i < fileSize; i++) {
                    blocks.get(spaceStart + i).setId(blocks.get(lastFullIndex - i).getId());
                    blocks.get(lastFullIndex - i).setId(null);
                }
            }
            
            lastFullIndex -= fileSize;
            lastId = nextId;
            
//            System.out.println(blocks.stream().map(Block::toString).collect(Collectors.joining()));
        }
        
        System.out.println(String.format("File system checksum: %d", calculateChecksum(blocks)));
    }
    
    private static long calculateChecksum(List<Block> blocks) {
        long checksum = 0;
        for (long i = 0; i < blocks.size(); i++) {
            Block block = blocks.get((int) i);
            if (block.isAllocated()) {
                checksum += i * block.getId();
            }
        }
        
        return checksum;
    }
    
    private static List<Block> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        String line = reader.readLines(DayNine.class.getResource(filePath)).get(0);
        
        List<Block> blocks = new ArrayList<>();
        boolean isFile = true;
        int nextId = 0;
        for (int i = 0; i < line.length(); i++) {
            int length = Integer.valueOf(line.substring(i, i + 1));
            if(isFile) {
                for (int j = 0; j < length; j++) {
                    blocks.add(new Block(nextId));
                }
                nextId++;
            } else {
                for (int j = 0; j < length; j++) {
                    blocks.add(new Block());
                }
            }
            isFile = !isFile;
        }
        
        return blocks;
    }

}
