package Model;

public class SpecialCatcher extends Catcher {
	

	@Override
    public void addTypeAddressMap(Object instance, String address) {
        throw new UnsupportedOperationException("Cannot add to typeAddressMap.");
    }
}