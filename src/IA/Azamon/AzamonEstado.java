package IA.Azamon;
import java.util.*;

public class AzamonEstado {

	//Atributos
	static Paquetes paq;
	static Transporte trans;
	private ArrayList<Integer> asignacion;
	private ArrayList<Double> capActual; //capacidad actual de cada oferta
	
	public AzamonEstado(int npaq, int seedpaq, double proporcion, int seedtrans) {
		asignacion = new ArrayList<Integer>(npaq);
		for (int i = 0; i < npaq; i++) {
			asignacion.add(-1);
		}
		paq = new Paquetes(npaq, seedpaq);
		trans = new Transporte(paq, proporcion, seedtrans);
		capActual = new ArrayList<Double>(trans.size());
		for (int i = 0; i < trans.size(); i++) {
			capActual.add(0.0);
		}
	}

	//Métodos
	
	public boolean isGoalState() {
		return false;
	}

	public void generaSolInicial1() {
		Collections.sort(paq, new Comparator<Paquete>() {
		    @Override
		    public int compare(Paquete p1, Paquete p2) {
		        return ((Integer)p1.getPrioridad()).compareTo((Integer)p2.getPrioridad());
		    }
		});
		
		
		ArrayList<Boolean> asignados = new ArrayList<Boolean>(paq.size());
		for (int i = 0; i < paq.size(); i++) asignados.add(false);
		
		int npaq = 0;
		for (int j = 0; j < 3; j++) {

            System.out.println("Pasada "+j+"\n");

			for(int i = 0; i < trans.size(); i++) {

                System.out.println("Oferta "+i+"\n");

				if (j == 0 && trans.get(i).getDias() == 1) {

                    while (capActual.get(i) < trans.get(i).getPesomax() && npaq < paq.size()) {
                        if (!asignados.get(npaq) && capActual.get(i)+paq.get(npaq).getPeso() <= trans.get(i).getPesomax()) {
                            asignacion.set(npaq, i);
                            asignados.set(npaq, true);
                            capActual.set(i, capActual.get(i)+paq.get(npaq).getPeso());
                            //System.out.println("Paquete " + npaq + " a oferta " + i + "\n");
                        }
                        //else System.out.println("Paquete ya asignado :) \n");
                    }
                }
				if (j == 1 && trans.get(i).getDias() <= 3) {
					while (capActual.get(i) < trans.get(i).getPesomax() && npaq < paq.size()) {
						if(!asignados.get(npaq) && capActual.get(i)+paq.get(npaq).getPeso() <= trans.get(i).getPesomax()) {
							asignacion.set(npaq, i);
							asignados.set(npaq, true);
                            capActual.set(i, capActual.get(i)+paq.get(npaq).getPeso());
                           // System.out.println("Paquete " + npaq + " a oferta " + i + "\n");
						}
                        npaq++;
                        //else System.out.println("Paquete ya asignado :) \n");
					}
				}
				
				if (j == 2 && trans.get(i).getDias() <= 5) {
                    while (capActual.get(i) < trans.get(i).getPesomax() && npaq < paq.size()) {
                        if (!asignados.get(npaq) && capActual.get(i)+paq.get(npaq).getPeso() <= trans.get(i).getPesomax()) {
                            asignacion.set(npaq, i);
                            asignados.set(npaq, true);
                            capActual.set(i, capActual.get(i)+paq.get(npaq).getPeso());
                            //System.out.println("Paquete " + npaq + " a oferta " + i + "\n");
                        }
                        npaq++;
                        //else System.out.println("Paquete ya asignado :) \n");
                    }
                }
				
			}
            npaq = 0;
		}
	}

	public boolean moverPaquete (int p, int o) {
		if (capActual.get(o)+paq.get(p).getPeso() <= trans.get(o).getPesomax()) {
			capActual.set(o, capActual.get(o)+paq.get(p).getPeso());
			asignacion.set(p, o);
			return true;
		}
		return false;
		//p -> id paquete en Paquetes, o -> id oferta en Transporte
		
	}
	
	public boolean permutarPaquetes (int p1, int p2) {
		int o1 = asignacion.get(p1);
		int o2 = asignacion.get(p2);
		if (capActual.get(o1)+paq.get(p2).getPeso()-paq.get(p1).getPeso() <= trans.get(o1).getPesomax()
				&& capActual.get(o2)+paq.get(p1).getPeso()-paq.get(p2).getPeso() <= trans.get(o2).getPesomax()) {
			capActual.set(o1, capActual.get(o1)+paq.get(p2).getPeso()-paq.get(p1).getPeso());
					capActual.set(o2, capActual.get(o2)+paq.get(p1).getPeso()-paq.get(p2).getPeso());
			asignacion.set(p1, o2);
			asignacion.set(p2, o1);
			return true;
		}
		return false;
		//p1/p2 -> id paquete p1/p2 en Paquetes
	}
	
	public boolean anadirPaquete (int p, int o) {
		if (capActual.get(o)+paq.get(p).getPeso() <= trans.get(o).getPesomax()) {
			capActual.set(o, capActual.get(o)+paq.get(p).getPeso());
			asignacion.set(p, o);
			return true;
		}
		return false;
	}
	
	public void quitarPaquete (int p) {
		int o = asignacion.get(p);
		capActual.set(o, capActual.get(o)-paq.get(p).getPeso());
		asignacion.set(p, -1);
		
	}
	
	public int hashFunction() {
		return 0;
	}
	
	public boolean equals(AzamonEstado ae) {
		return false;
	}
	
	public String toString() {
		StringBuffer resultado = new StringBuffer();
		resultado.append("Asignación: \n");
		for (int i = 0; i < asignacion.size(); i++) {
			resultado.append("Paquete "+i+": "+paq.get(i).getPeso()+"kg/PR"+paq.get(i).getPrioridad());
			resultado.append(" - Oferta "+asignacion.get(i)+"\n");
		}
		resultado.append("Ofertas: \n");
		for (int i = 0; i < trans.size(); i++) {
			resultado.append("Oferta " + i + ": " + trans.get(i).getDias() + " días, " + capActual.get(i) + "/" + trans.get(i).getPesomax() + "kg, " + trans.get(i).getPrecio() + " €/kg \n");
		}
		return resultado.toString();
	}
	
	public double getCapActualOferta(int i) {
		return capActual.get(i);
	}
	
	public int getOfertaPaquete(int i) {
		return asignacion.get(i);
	}
	
}
