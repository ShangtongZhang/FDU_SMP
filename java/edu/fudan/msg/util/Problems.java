package edu.fudan.msg.util;

import java.util.HashMap;
import java.util.Iterator;

public class Problems {
	public int[] twoSum(int[] numbers, int target) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i = 0; i < numbers.length; ++ i) {
            int num = numbers[i];
            if(map.containsKey(target - num)){
                return new int[]{ map.get(target - num), i + 1}; 
            }else if(!map.containsKey(num)){
                map.put(num, i + 1);    
            }
        }
        int[] result = new int[2];
        Iterator<Integer> it = map.keySet().iterator();
        while(it.hasNext()){
            Integer key = it.next(), remain = target - key;
            if(map.containsKey(remain)){
            	int index1 = map.get(key), index2 = map.get(remain);
            	if(index1 < index2){ result[0] = index1; result[1] = index2; }
            	else { result[0] = index1; result[1] = index2; }
            }
        }
        return result;
    }
	
	public static int[] preprocess(String s){
		StringBuilder sb = new StringBuilder(s);
        int len = s.length();
        for(int i = len - 2; i >= 0;  -- i){ sb.insert(i + 1, '#'); }
        String ps = sb.toString();
        int plen = ps.length();
        int[] maxlen = new int[plen];
        maxlen[0] = 1;
        int mx = 0, pos = 0;
        for(int i = 1; i < plen; ++ i){
            int tmp, j = 2 * pos - i;
            if(j < 0){ tmp = i + 1; }
            else {
                if(mx > maxlen[j] + i - 1){ tmp = maxlen[j] + i; }
                else { tmp = mx + 1; }
            }
            while(true){
                int prev = 2 * i - tmp;
                if(tmp < plen && prev >= 0 && ps.charAt(tmp) == ps.charAt(prev)){ tmp ++; }
                else{ break; }
            }
            maxlen[i] = tmp - i;
            if(tmp > mx){ mx = tmp - 1; pos = i; }
        }
        return maxlen;
    }
	
	public static boolean[][] preprocess(String s, final int len){
        boolean[][] result = new boolean[len][len];
        for(int i = 0; i < len; i ++){ 
            result[i][i] = true; 
            if((i + 1 < len) && s.charAt(i) == s.charAt(i + 1)){ result[i][i + 1] = true; }
        }
        for(int i = 3; i <= len; ++ i){
            for(int index = 0, end; (end = index + i - 1) < len; ++ index){
            	if(result[index + 1][end - 1] && s.charAt(index) == s.charAt(end)){ result[index][end] = true; }
            }
        }
        return result;
    }
	public static void main(String args[]){
		String s = "1052254545";
//		preprocess(test, test.length());
		int x = Integer.parseInt(s);
		System.out.println(x * 10);
	}
}
