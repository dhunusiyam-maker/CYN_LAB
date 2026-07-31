import java.util.*;

public class PlayfairEngine {
    private char[][] matrix = new char[5][5];

    public PlayfairEngine(String key) {
        boolean[] visited = new boolean[26];
        String preparedKey = key.toLowerCase().replace("j", "i").replaceAll("[^a-z]", "");
        StringBuilder sb = new StringBuilder();

        for (char c : preparedKey.toCharArray()) {
            if (!visited[c - 'a']) {
                visited[c - 'a'] = true;
                sb.append(c);
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (c == 'j') continue;
            if (!visited[c - 'a']) {
                sb.append(c);
            }
        }

        int index = 0;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                matrix[r][c] = sb.charAt(index++);
            }
        }
    }

    private int[] getPosition(char c) {
        if (c == 'j') c = 'i';
        for (int r = 0; r < 5; r++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[r][col] == c) return new int[]{r, col};
            }
        }
        return new int[]{-1, -1};
    }

    public String process(String text, boolean encrypt) {
        text = text.toLowerCase().replace("j", "i").replaceAll("[^a-z]", "");
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i));
            if (encrypt && i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1)) {
                sb.append('x'); 
            }
        }
        if (sb.length() % 2 != 0) sb.append('x');

        StringBuilder result = new StringBuilder();
        int direction = encrypt ? 1 : 4; 

        for (int i = 0; i < sb.length(); i += 2) {
            int[] pos1 = getPosition(sb.charAt(i));
            int[] pos2 = getPosition(sb.charAt(i + 1));

            if (pos1[0] == pos2[0]) { 
                result.append(matrix[pos1[0]][(pos1[1] + direction) % 5]);
                result.append(matrix[pos2[0]][(pos2[1] + direction) % 5]);
            } else if (pos1[1] == pos2[1]) { 
                result.append(matrix[(pos1[0] + direction) % 5][pos1[1]]);
                result.append(matrix[(pos2[0] + direction) % 5][pos2[1]]);
            } else { // Rectangle Corners
                result.append(matrix[pos1[0]][pos2[1]]);
                result.append(matrix[pos2[0]][pos1[1]]);
            }
        }
        return result.toString().toUpperCase();
    }
}
