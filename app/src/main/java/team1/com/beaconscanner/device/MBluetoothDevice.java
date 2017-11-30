package team1.com.beaconscanner.device;

/**
 * Created by jan on 11/29/17.
 */

public class MBluetoothDevice {
    private String address;
    private String name;
    private Short rssi;


    public MBluetoothDevice() {
    }

    public Short getRssi() {
        return rssi;
    }

    public void setRssi(Short rssi) {
        this.rssi = rssi;
    }

    public MBluetoothDevice(String address, String name, Short rssi) {
        this.address = address;
        this.name = name;
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return  new MBluetoothDevice(
              this.getAddress(), this.getName(), this.getRssi()
        );
    }
}
