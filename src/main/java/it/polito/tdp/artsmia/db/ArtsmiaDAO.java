package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> listRoles() {
		String sql = "SELECT DISTINCT role from authorship ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> listArtistsWithRole(String role) {
		String sql = "SELECT DISTINCT a.artist_id from authorship a WHERE role = ? ";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
			
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getInt("a.artist_id"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> listArchi(String role) {
		String sql = "SELECT a1.artist_id, a2.artist_id, COUNT(DISTINCT eo1.object_id, eo2.object_id) as weight " +
		"FROM authorship a1, authorship a2,  exhibition_objects eo1, exhibition_objects eo2 " +
		"WHERE a1.artist_id < a2.artist_id AND a1.role = ? AND a2.role = ? AND eo1.exhibition_id=eo2.exhibition_id and a1.object_id = eo1.object_id AND a2.object_id = eo2.object_id"
		+ " GROUP BY a1.artist_id, a2.artist_id ";
		
				List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
			
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			st.setString(2, role);

			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Adiacenza(res.getInt("a1.artist_id"), res.getInt("a2.artist_id"), res.getDouble("weight")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean findArtist(Integer a) {
		String sql = "SELECT DISTINCT artist_id FROM artists WHERE artist_id = ? ";
				
						List<Adiacenza> result = new ArrayList<>();
				Connection conn = DBConnect.getConnection();
					
				try {
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, a);

					ResultSet res = st.executeQuery();
					
					if (res.next()) {
						conn.close();
						return true;
					}
					
					conn.close();
					return false;
					
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
	}
}
