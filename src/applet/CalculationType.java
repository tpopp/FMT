/**
 * 
 */
package applet;

/**
 * @author Tres
 *
 */
public enum CalculationType {
	DensityProfile("Density Profile", "External Interaction"),
	ExternalInteraction("External Interaction", "Density Profile"),
	TracerInteraction("Tracer Interaction", "Other");
	
	public final String inputTitle;
	public final String outputTitle;
	
	CalculationType(String outTitle, String inTitle){
		this.inputTitle = inTitle;
		this.outputTitle = outTitle;
	}
	
	public enum ProfileType{
		ConfinedSystem(),
		SinusoidalSystem();
	}
}
