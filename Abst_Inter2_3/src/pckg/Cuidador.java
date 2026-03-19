package pckg;

public class Cuidador {
	
	String nombre;
	String aniosExperiencia;
	String especialidad;
	
	Cuidador(String nom,String anios,String esp){
		this.nombre=nom;
		this.aniosExperiencia=anios;
		this.especialidad=esp;
	}
	
	void setnombre(String x) {
		this.nombre=x;
	}
	void setaniosExperiencia(String x) {
		this.aniosExperiencia=x;
	}
	void setespecialidad(String x) {
		this.especialidad=x;
	}
	String getnombre() {
		return this.nombre;
	}
	String getaniosExperiencia() {
		return this.aniosExperiencia;
	}
	String getespecialidad() {
		return this.especialidad;
	}

}
