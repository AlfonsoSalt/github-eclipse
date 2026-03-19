package pckg;

public class Habitat {
	String tipo;
	String temperaturaPromedio;
	String region;
	
	Habitat(String tip,String tem,String reg){
		this.tipo=tip;
		this.temperaturaPromedio=tem;
		this.region=reg;
	}
	
	
	void settipo(String x) {
		this.tipo=x;
	}
	void settemperaturaPromedio(String x) {
		this.temperaturaPromedio=x;
	}
	void setregion(String x) {
		this.region=x;
	}
	String gettipo() {
		return this.tipo;
	}
	String gettemperaturaPromedio() {
		return this.temperaturaPromedio;
	}
	String getregion() {
		return this.region;
	}

}
