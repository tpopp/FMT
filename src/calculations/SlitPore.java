/**
 * 
 */
package calculations;

import java.util.ArrayList;

/**
 * @author Tres
 * 
 */
public class SlitPore {

	/*
	 * Uses FMT theory to calculate the equilibrium density profiles for a fluid
	 * confined in a slit-pore
	 */

	// global_variables
	double H_sc, phi, Dia, mesh_sc;
	double PI = 4.0 * Math.atan(1.0);
	int size, start;
	ArrayList<Wd> N_array;
	// Util.Vec<Wd> N_array;
	// Util.Vec<Double> rho_sc, DCF, Exp_Potential; //Exp_Potential is the
	// boltzmann factor
	ArrayList<Double> rho_sc, DCF, Exp_Potential; // Exp_Potential is the
													// boltzmann factor

	int main(String[] args) {

		int ilb, iub, PMAX, External_rhoFileFLAG, External_potFileFLAG, rho_colA, rho_colB, pot_colA, pot_colB;
		double rho_bulk, rho_CA, n0, n1, n2, n3, nv1, nv2, SC, betamu_excess, fugacity;
		double error, Kappa, Kappa_min, Kappa_max;
		double s_excess;
		String rhofilename, potfilename;
		ArrayList<Double> rho1_sc, rho2_sc, Diff;

		// Input Parameters
		H_sc = Double.parseDouble(args[0]); // Height of the slit pore scaled by
											// the particle diameter
		Dia = Double.parseDouble(args[1]); // Particle diameter. In all cases it
											// is 1.0
		phi = Double.parseDouble(args[2]); // volume fraction of the particles
											// within the pore
		External_rhoFileFLAG = Integer.parseInt(args[3]); // If you want to have
															// a starting
															// density matrix,
															// then use this
															// option as 0,
															// otherwise 1. In
															// the latter case,
															// it will
															// initialize the
															// matrix to its
															// default value
		External_potFileFLAG = Integer.parseInt(args[4]); // Specify the
															// external
															// potential file

		if (External_rhoFileFLAG == 0) {
			rhofilename = args[5];
			rho_colA = Integer.parseInt(args[6]);
			rho_colB = Integer.parseInt(args[7]);
		}

		if (External_potFileFLAG == 0) {
			potfilename = args[5 + 3 * (1 - External_rhoFileFLAG)];
			pot_colA = Integer
					.parseInt(args[5 + 3 * (1 - External_rhoFileFLAG) + 1]);
			pot_colB = Integer
					.parseInt(args[5 + 3 * (1 - External_rhoFileFLAG) + 2]);
		}

		rho_bulk = 6.0 * phi / PI; // the average density; it is also called the
									// surface accessible density
		rho_CA = rho_bulk * H_sc / (H_sc - 1.0); // the density calculated just
													// for the centre of the
													// hard spheres; i.e. by
													// using the volume that is
													// accessible to the sphere
													// centers.

		// XXX: mesh size can be changed here
		mesh_sc = 0.01; // discretize the pore height with a mesh size of
		size = (int) (H_sc / mesh_sc) + 1; // total size of array
		start = (int) ((0.5 * Dia) / mesh_sc); // the index corresponding to the
												// radius of the sphere
		SC = mesh_sc * Dia * Math.pow(Dia, -3.0); // Just a factor that will be
													// used during the
													// integration
		ilb = -start; // lower bound of the integrals
		iub = start; // upper bound of the itnegrals
		betamu_excess = phi * (8.0 - 9.0 * phi + 3.0 * phi * phi)
				* Math.pow(1.0 - phi, -3.0); // mu_excess calculated as per the
												// Carnahan-Starling equation of
												// state
		PMAX = 100000; // max number of Picard iterations.

		// Sizing up the arrays involved
		N_array = new ArrayList<Wd>(size);
		rho_sc = new ArrayList<Double>(size);
		Exp_Potential = new ArrayList<Double>(size);
		rho1_sc = new ArrayList<Double>(size);
		rho2_sc = new ArrayList<Double>(size);
		Diff = new ArrayList<Double>(size);
		DCF = new ArrayList<Double>(size);

		// initialising matrices
		if (External_rhoFileFLAG == 1) {
			for (int i = 0; i < start; i++) {
				rho_sc.set(i, 0.0);
				rho_sc.set(size - 1 - i, 0.0);
			}

			for (int i = start; i < size - start; i++)
				rho_sc.set(i, rho_bulk);
		}
		// else {
		// ArrayList<Double> array;
		// // readarray(array,rhofilename,rho_colA,rho_colB);
		//
		// /*
		// if ((array.size()/2)!=size){
		// cerr << "MISMATCH IN INPUT FILE AND INPUT RHO ARRAY!\n"
		// << "Check mesh size/ input file initial & final index" << endl;
		// exit(0);
		// }
		// */
		//
		// for(int i=1;i<array.size();i=i+2)
		// rho_sc.set((i-1)/2, array.get(i))[(i-1)/2]=array[i];
		//
		// for(int i=0;i<start;i++){
		// rho_sc[i]=0.;
		// rho_sc[size-1-i]=0.;
		// }
		// }

		// Initialising the external potential matrix
		if (External_potFileFLAG == 1) {
			for (int i = start; i < size - start; i++)
				Exp_Potential.set(i, 1.0);

			for (int i = 0; i < start; i++) {
				Exp_Potential.set(i, 0.0);
				Exp_Potential.set(size - 1 - i, 0.0);
			}
		}
		// else {
		// Util<Double>.Vec<Double> array;
		// int index;
		// // readarray(array,potfilename,pot_colA,pot_colB);
		//
		// /*
		// if ((array.size()/2)!=size){
		// cerr << "MISMATCH IN INPUT FILE AND INPUT RHO ARRAY!\n"
		// << "Check mesh size/ input file initial & final index" << endl;
		// exit(0);
		// }
		// */
		//
		// for(int i=1;i<array.size();i=i+2){
		// index=(array[i-1]*1E4 + 1E1)/(mesh_sc*1E4);
		// Exp_Potential[index]=exp(-array[i]);
		// }
		//
		// for(int i=0;i<start;i++){
		// Exp_Potential[i]=0.;
		// Exp_Potential[size-1-i]=0.;
		// }
		// }

		DCF = new ArrayList<Double>(rho_sc);
		rho2_sc = new ArrayList<Double>(rho_sc);

		for (int iP = 0; iP < PMAX; iP++) {

			// Find the weighted densities
			for (int iz = 0; iz < size; iz++) {
				n2 = integrate_n2z(ilb, iub, iz);
				n2 = n2 * SC * PI * Dia;

				n3 = integrate_n3z(ilb, iub, iz);
				n3 = PI * SC * n3;

				nv2 = integrate_nv2z(ilb, iub, iz);
				nv2 = -2.0 * PI * SC * nv2;

				n0 = n2 / (PI * Dia * Dia);
				n1 = n2 / (2.0 * PI * Dia);
				nv1 = nv2 / (2.0 * PI * Dia);

				N_array.get(iz).set_values(n0, n1, n2, n3, nv1, nv2);
			}

			// Find the direct correlation function, DCF. Note that it's
			// calculated from r to H-r
			for (int iz = start; iz < size - start; iz++) {
				DCF.set(iz, integrate_DCF(ilb, iub, iz));
			}

			// fugacity is exp(beta*mu). using the fact that
			// integral(rho(z))=rho_avg
			fugacity = H_sc
					/ (integrate_DCFcheck(start, size - 1 - start, 0) * mesh_sc);

			// find the new density as per the iteration
			for (int iz = start; iz < size - start; iz++)
				rho2_sc.set(iz, rho_bulk * Math.exp(DCF.get(iz))
						* Exp_Potential.get(iz) * fugacity);

			Diff = rho2_sc;
			if (Diff.size() != rho2_sc.size()) {
				Diff = new ArrayList<Double>(size);
			}
			for (int i = 0; i < Diff.size(); i++)
				Diff.set(i, Diff.get(i) - rho2_sc.get(i));
			error = 0.;
			error = max_error(error, Diff);

			rho1_sc = new ArrayList<Double>(rho2_sc);
			rho_sc = new ArrayList<Double>(rho2_sc);

			// get Kappa
			if (iP < 10)
				Kappa_min = 1E-3;
			else {
				if (error > 1E-3)
					Kappa_min = 5E-3;
				else if ((error < 1E-3) && (error > 1E-6))
					Kappa_min = 1E-2;
				else
					Kappa_min = 5E-2;
			}

			// mix the densities
			for (int ik = start; ik < (size - start); ik++) {
				n3 = integrate_n3z(ilb, iub, ik);
				if (n3 >= 1.)
					n3 = (1.0 - 1E-3);
				Kappa_max = Math.min(
						1.0,
						Math.abs((1 - N_array.get(ik).ret_n3())
								/ (n3 - N_array.get(ik).ret_n3())));
				Kappa = Math.min(Kappa_min, 0.9 * Kappa_max);
				rho_sc.set(ik, rho1_sc.get(ik) + Kappa
						* (rho2_sc.get(ik) - rho1_sc.get(ik)));
			}

			/*
			 * //print as per the iteration if ((iP%1000)==0){ out <<
			 * "density_at_p_"<<iP<<"_phi_"<<phi<<"_H_"<<H_sc<<".dat"; ofstream
			 * fout ((out.str()).c_str());
			 * 
			 * for(int iz=0;iz<N_array.size();iz++) fout << setw(10) <<
			 * iz*mesh_sc*Dia << setw(15) << rho_sc[iz] << setw(15) <<
			 * rho_sc[iz]/rho_bulk << setw(15) << DCF[iz] << setw(15) <<
			 * Exp_Potential[iz] << endl;
			 * 
			 * fout << "##ERROR " << error << endl; fout << "S_excess/N = " <<
			 * setw(10) <<
			 * -1.0*mesh_sc*integrate(integrand_freeenergy,start,size
			 * -1-start,0)/(rho_bulk*H_sc) << endl; fout << "activity = " <<
			 * setw(10) <<
			 * rho_bulk*H_sc/(integrate(integrand_DCFcheck,start,size
			 * -1-start,0)*mesh_sc) << endl;
			 * 
			 * out.str(""); }
			 */

			if (error < 1E-8)
				break;
		}

		/*
		 * out << "final_density_phi_"<<phi<<"_H_"<<H_sc<<".dat"; ofstream fout
		 * ((out.str()).c_str());
		 * 
		 * for(int iz=start;iz<N_array.size()-start;iz++) fout << setw(10) <<
		 * iz*mesh_sc*Dia << setw(15) << rho_sc[iz] << setw(15) <<
		 * rho_sc[iz]/rho_bulk << setw(10) << DCF[iz] << setw(10) <<
		 * Exp_Potential[iz] << endl; fout << "##ERROR " << error << endl;
		 * 
		 * //S_excess is the excess entropy = -1 * Free Energy fout <<
		 * "S_excess/N = " << setw(10) <<
		 * -1.0*Dia*integrate(integrand_freeenergy
		 * ,start,size-1-start,0)/integrate(integrand_n2z,start,size-1-start,0)
		 * << endl; fout << "activity = " << setw(10) <<
		 * rho_bulk*H_sc/(integrate
		 * (integrand_DCFcheck,start,size-1-start,0)*mesh_sc) << endl;
		 */

		return 0;
	}

	private double integrate_n2z(int ilb, int iub, int iz) {

		double sum;
		sum = 0.;

		if (ilb == iub)
			sum = 0;
		else if (iub == ilb + 1) {
			sum = 0.5 * (integrand_n2z(ilb, iz) + integrand_n2z(iub, iz));
		} else {
			// Initialise sum
			ArrayList<Double> Integrand = new ArrayList<Double>(iub - ilb + 1);
			for (int i = 0; i < iub - ilb + 1; i++) {
				Integrand.set(i, 0.0);
			}

			for (int izd = ilb; izd <= iub; izd++)
				Integrand.set(izd - ilb, integrand_n2z(izd, iz));

			// trapezoidal rule
			for (int izd = 1; izd < (iub - ilb); izd++)
				sum += Integrand.get(izd);

			sum += 0.5 * (integrand_n2z(ilb, iz) + integrand_n2z(iub, iz));
		}

		if (Math.abs(sum) < 1E-10)
			sum = 0.;

		return sum;
	}

	private double integrate_nv2z(int ilb, int iub, int iz) {

		double sum;
		sum = 0.;

		if (ilb == iub)
			sum = 0;
		else if (iub == ilb + 1) {
			sum = 0.5 * (integrand_nv2z(ilb, iz) + integrand_nv2z(iub, iz));
		} else {
			// Initialise sum
			ArrayList<Double> Integrand = new ArrayList<Double>(iub - ilb + 1);
			for (int i = 0; i < iub - ilb + 1; i++) {
				Integrand.set(i, 0.0);
			}

			for (int izd = ilb; izd <= iub; izd++)
				Integrand.set(izd - ilb, integrand_nv2z(izd, iz));

			// trapezoidal rule
			for (int izd = 1; izd < (iub - ilb); izd++)
				sum += Integrand.get(izd);

			sum += 0.5 * (integrand_nv2z(ilb, iz) + integrand_nv2z(iub, iz));
		}

		if (Math.abs(sum) < 1E-10)
			sum = 0.;

		return sum;
	}

	private Double integrate_DCF(int ilb, int iub, int iz) {

		double sum;
		sum = 0.;

		if (ilb == iub)
			sum = 0;
		else if (iub == ilb + 1) {
			sum = 0.5 * (integrand_DCF(ilb, iz) + integrand_DCF(iub, iz));
		} else {
			// Initialise sum
			ArrayList<Double> Integrand = new ArrayList<Double>(iub - ilb + 1);
			for (int i = 0; i < iub - ilb + 1; i++) {
				Integrand.set(i, 0.0);
			}

			for (int izd = ilb; izd <= iub; izd++)
				Integrand.set(izd - ilb, integrand_DCF(izd, iz));

			// trapezoidal rule
			for (int izd = 1; izd < (iub - ilb); izd++)
				sum += Integrand.get(izd);

			sum += 0.5 * (integrand_DCF(ilb, iz) + integrand_DCF(iub, iz));
		}

		if (Math.abs(sum) < 1E-10)
			sum = 0.;

		return sum;
	}

	private double integrate_DCFcheck(int ilb, int iub, int iz) {

		double sum;
		sum = 0.;

		if (ilb == iub)
			sum = 0;
		else if (iub == ilb + 1) {
			sum = 0.5 * (integrand_DCFcheck(ilb, iz) + integrand_DCFcheck(iub,
					iz));
		} else {
			// Initialise sum
			ArrayList<Double> Integrand = new ArrayList<Double>(iub - ilb + 1);
			for (int i = 0; i < iub - ilb + 1; i++) {
				Integrand.set(i, 0.0);
			}

			for (int izd = ilb; izd <= iub; izd++)
				Integrand.set(izd - ilb, integrand_DCFcheck(izd, iz));

			// trapezoidal rule
			for (int izd = 1; izd < (iub - ilb); izd++)
				sum += Integrand.get(izd);

			sum += 0.5 * (integrand_DCFcheck(ilb, iz) + integrand_DCFcheck(iub,
					iz));
		}

		if (Math.abs(sum) < 1E-10)
			sum = 0.;

		return sum;
	}

	private double integrate_n3z(int ilb, int iub, int iz) {

		double sum;
		sum = 0.;

		if (ilb == iub)
			sum = 0;
		else if (iub == ilb + 1) {
			sum = 0.5 * (integrand_n3z(ilb, iz) + integrand_n3z(iub, iz));
		} else {
			// Initialise sum
			ArrayList<Double> Integrand = new ArrayList<Double>(iub - ilb + 1);
			for (int i = 0; i < iub - ilb + 1; i++) {
				Integrand.set(i, 0.0);
			}

			for (int izd = ilb; izd <= iub; izd++)
				Integrand.set(izd - ilb, integrand_n3z(izd, iz));

			// trapezoidal rule
			for (int izd = 1; izd < (iub - ilb); izd++)
				sum += Integrand.get(izd);

			sum += 0.5 * (integrand_n3z(ilb, iz) + integrand_n3z(iub, iz));
		}

		if (Math.abs(sum) < 1E-10)
			sum = 0.;

		return sum;
	}

	// Two different ways of calculating the error.
	double rms_error(double error, ArrayList<Double> Diff) {
		error = 0.;
		for (int i = start; i < (size - start); ++i)
			error += Math.pow(Diff.get(i), 2.);
		error = error / (size - 2 * start);
		return error;
	}

	double max_error(double error, ArrayList<Double> Diff) {
		double maxval = Math.abs(Diff.get(start));
		for (int i = start; i < (size - start); ++i) {
			if (Math.abs(Diff.get(i)) > maxval)
				maxval = Math.abs(Diff.get(i));
		}
		return error;
	}

	// Trapezoidal rule to integrate
	/*
	 * double integrate(double func( int izdash, int iz), int ilb, int iub, int
	 * iz){
	 * 
	 * double sum; sum=0.;
	 * 
	 * if (ilb==iub) sum=0; else if (iub==ilb+1) {
	 * sum=0.5*(func(ilb,iz)+func(iub,iz)); } else { //Initialise sum
	 * Util<Double>.Vec<Double> Integrand(0.,iub-ilb+1);
	 * 
	 * for(int izd=ilb;izd<=iub;izd++) Integrand[izd-ilb]=func(izd,iz);
	 * 
	 * //trapezoidal rule for(int izd=1;izd<(iub-ilb);izd++)
	 * sum+=Integrand[izd];
	 * 
	 * sum+=0.5*(func(ilb,iz)+func(iub,iz)); }
	 * 
	 * if (Math.abs(sum)<1E-10) sum=0.;
	 * 
	 * return sum; }
	 */

	double integrand_DCFcheck(int izdash, int iz) {
		int counter;
		double res;
		counter = izdash + iz;

		if ((counter < start) || (counter > (size - 1 - start)))
			res = 0.;
		else
			res = Math.exp(DCF.get(counter)) * Exp_Potential.get(counter);

		return res;
	}

	// Using the White Bear I functional
	double integrand_freeenergy(int izdash, int iz) {
		int counter;
		double res, n0, n1, n2, n3, nv1, nv2;
		counter = izdash + iz;

		if ((counter < start) || (counter > (size - 1 - start)))
			res = 0.;
		else {
			n0 = N_array.get(counter).ret_n0();
			n1 = N_array.get(counter).ret_n1();
			n2 = N_array.get(counter).ret_n2();
			n3 = N_array.get(counter).ret_n3();
			nv1 = N_array.get(counter).ret_nv1();
			nv2 = N_array.get(counter).ret_nv2();
			res = (-n0 * Math.log(1.0 - n3))
					+ (n1 * n2 - nv1 * nv2)
					* Math.pow(1.0 - n3, -1.0)
					+ (Math.pow(n2, 3.0) - 3.0 * n2 * nv2 * nv2)
					* (n3 + Math.pow(1.0 - n3, 2.0) * Math.log(1.0 - n3))
					* Math.pow(36 * PI * n3 * n3 * (1.0 - n3) * (1.0 - n3),
							-1.0);
		}

		return res;
	}

	double integrand_n2z(int izdash, int iz) {

		int counter;
		double res;

		counter = iz + izdash;

		if ((counter < start) || (counter > (size - 1 - start)))
			res = 0.;
		else
			res = rho_sc.get(counter);

		if (Math.abs(res) < 1E-10)
			res = 0.;
		return res;
	}

	double integrand_n3z(int izdash, int iz) {
		int counter;
		double zdash, res, eps;

		eps = 0.25 * mesh_sc * Dia;

		counter = iz + izdash;
		zdash = izdash * mesh_sc * Dia;

		if ((counter < start) || (counter > (size - 1 - start)))
			res = 0.;
		else if ((iz == 0) && (izdash == start))
			res = eps * (Dia + eps) * eps * rho_sc.get(start + 1)
					/ (mesh_sc * Dia);
		else if ((iz == size - 1) && (izdash == -start))
			res = eps * (Dia + eps) * eps * rho_sc.get(start + 1)
					/ (mesh_sc * Dia);
		else
			res = (rho_sc.get(counter)) * (0.25 * Dia * Dia - zdash * zdash);

		if (Math.abs(res) < 1E-10)
			res = 0.;
		return res;
	}

	double integrand_nv2z(int izdash, int iz) {
		int counter;
		double zdash, res;

		counter = iz + izdash;
		zdash = izdash * mesh_sc * Dia;

		if ((counter < start) || (counter > (size - 1 - start)))
			res = 0.;
		else
			res = (rho_sc.get(counter)) * zdash;

		if (Math.abs(res) < 1E-10)
			res = 0.;
		return res;
	}

	double integrand_DCF(int izdash, int iz) { // izdash is the dummy variable
		int counter;
		double n0, n1, n2, n3, nv1, nv2, res, iDia, zdash;

		counter = iz + izdash;

		if ((counter < 0) || (counter > size - 1))
			res = 0;
		else {
			iDia = 1.0 / Dia;
			zdash = izdash * mesh_sc * Dia;
			n0 = N_array.get(counter).ret_n0();
			n1 = N_array.get(counter).ret_n1();
			n2 = N_array.get(counter).ret_n2();
			n3 = N_array.get(counter).ret_n3();
			nv1 = N_array.get(counter).ret_nv1();
			nv2 = N_array.get(counter).ret_nv2();

			/*
			 * if (n3<1E-10){ cout << "n3 is really close to 0" << endl; }
			 */

			// res = (dphi/dn_j)*nj for j=0,1,2,3,v1,v2

			res = (-iDia * Math.log(1.0 - n3));
			res += (0.5 * n2 / (1 - n3));
			res += PI * Dia * (n1 / (1.0 - n3));
			res += (1.0 / (12. * PI * n3 * n3)) * (n2 * n2 - nv2 * nv2)
					* (Math.log(1.0 - n3) + n3 * Math.pow(1.0 - n3, -2.0));
			res += PI
					* (0.25 * Dia * Dia - zdash * zdash)
					* (n0 * Math.pow(1.0 - n3, -1.0) + (n1 * n2 - nv1 * nv2)
							* Math.pow(1.0 - n3, -2.0) + (n2 / (36.0 * PI))
							* (3.0 * nv2 * nv2 - n2 * n2)
							* ((2.0 - 5.0 * n3 + n3 * n3)
									* Math.pow(n3 * (1.0 - n3), -2.0)
									* Math.pow(1 - n3, -1.0) + 2.0
									* Math.log(1.0 - n3) * Math.pow(n3, -3.0)));
			res += -(zdash * iDia * nv2 / (1.0 - n3));
			res += -(2.0 * PI * zdash)
					* (nv1 * Math.pow(1.0 - n3, -1.0) + (n2 * nv2)
							* (Math.pow(1.0 - n3, 2.0) * Math.log(1.0 - n3) + n3)
							/ (6.0 * PI * Math.pow(n3 * (1.0 - n3), 2.0)));
		}

		if (Math.abs(res) < 1E-10)
			res = 0.;
		return (-1.0 * res * mesh_sc * Dia);
	}

	/*
	 * void readarray(Util<Double>.Vec<Double> array, string &filename, int
	 * colA, int colB){ ifstream inp; string line; int nolines;
	 * 
	 * Util<Double>.Vec<Double> toread(0.,colB); vector<Double>
	 * colA_array,colB_array;
	 * 
	 * inp.open(filename.c_str());
	 * 
	 * nolines=0; while (true){ getline(inp,line); if (inp) { if (!(line[0]=='#'
	 * || line[0]=='-')) { istringstream iss(line); nolines++; for (int
	 * i=0;i<colB;i++) iss >> toread[i]; colA_array.push_back(toread[colA-1]);
	 * colB_array.push_back(toread[colB-1]); } continue; } if (inp.eof()) break;
	 * 
	 * cerr << "Stream is bad! Or the file does not exist" << "filename: " <<
	 * filename << endl; exit(1); }
	 * 
	 * array.resize(2*nolines); for(int i=0;i<nolines;i++){
	 * array[2*i]=colA_array[i]; array[2*i+1]=colB_array[i]; } }
	 */
}