import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestClass {

	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream(new File("KOgamelist"));
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		ObjectMapper objMapper = new ObjectMapper();
		HashMap resMap = objMapper.readValue(in, new  TypeReference<HashMap<String, Object>>(){});
		for(Map map : (ArrayList<Map>)resMap.get("StreetGame")) {
			System.out.println(map.get("GameCode")+"\t"+map.get("GameName"));
		}
	}
}
