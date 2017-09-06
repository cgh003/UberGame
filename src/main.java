
public class main {
	public static void main(String[] args) {
		Grid obj = new Grid (10,10);
		DummyController d = new DummyController(obj);
		SharedCar car = new SharedCar(d, obj);
		obj.addCar(car);
		System.out.println(obj);
		car.drive();
		System.out.println(obj);
		car.drive();
		System.out.println(obj);
	}
}
