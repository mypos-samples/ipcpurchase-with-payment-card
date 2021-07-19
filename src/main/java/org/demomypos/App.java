package org.demomypos;

import com.mypos.myposcheckout.ipc.*;
import com.mypos.myposcheckout.ipc.enumerable.CardTokenRequest;
import com.mypos.myposcheckout.ipc.enumerable.Currency;
import com.mypos.myposcheckout.ipc.enumerable.PaymentParametersRequired;
import com.mypos.myposcheckout.ipc.request.Purchase;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IPCException {
        Config cnf = new Config();

        URL ipcApiUrl = null;
        try {
            ipcApiUrl = new URL("https://mypos.eu/vmp/checkout-test/");
        } catch (MalformedURLException ex) {
            // Handle the malformed URL exception
        }

        cnf.setIpcUrl(ipcApiUrl);
        cnf.setLang("en");
        cnf.loadPrivateKeyFromFile("path_to_directory/storePrivateKey.pem"); // Replace `path_to_directory` with the actual file path
        cnf.loadPublicKeyFromFile("path_to_directory/apiPublicKey.pem"); // Replace `path_to_directory` with the actual file path
        cnf.setKeyIndex(1);
        cnf.setSid("000000000000010");
        cnf.setVersion("1.3");
        cnf.setWalletNumber("61938166610");


        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setEmail("demo@demo.demo");
        customer.setPhone("+359888123456");
        customer.setCountry("BGR");
        customer.setAddress("Business Park Varna");
        customer.setCity("Varna");
        customer.setZip("9000");


        Cart cart = new Cart();
        cart.addItem(new CartItem("Some Book", 101.23, 2));
        cart.addItem(new CartItem("Some other book", 1.25, 8));
        cart.addItem(new CartItem("Stuff", 0.63, 5));
        cart.addItem(new CartItem("Discount", -5.3, 1));


        Purchase purchase = new Purchase(cnf);
        try {
            purchase.setCancelUrl(new URL("https://mysite.com/ipc_cancel")); // User comes here after purchase cancelation
            purchase.setSuccessUrl(new URL("https://mysite.com/ipc_ok")); // User comes here after purchase success
            purchase.setNotifyUrl(new URL("https://mysite.com/ipc_notify")); // IPC sends POST reuquest to this address with purchase status
        } catch (MalformedURLException ex) {
            // Handle the malformed URL exception
        }
        purchase.setOrderId("123456"); // Some unique ID
        purchase.setCurrency(Currency.EUR);
        purchase.setNote("Some note"); // Not required
        purchase.setCardTokenRequest(CardTokenRequest.DO_NOT_REQUEST_TOKEN);
        purchase.setParametersRequired(PaymentParametersRequired.FULL_REQUEST);
        purchase.setCustomer(customer);
        purchase.setCart(cart);

        purchase.process();
    }
}
