package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.AddressRequest;
import lt.viko.eif.pvaiciulis.dto.response.AddressResponse;
import lt.viko.eif.pvaiciulis.model.UserModel.Address;
import lt.viko.eif.pvaiciulis.repository.AddressRepository;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponse getAddressForUser(Integer userId) {
        return addressRepository.findByUserId(userId)
                .map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(address.getZipCode())
                        .country(address.getCountry())
                        .success(true)
                        .build())
                .orElse(AddressResponse.builder()
                        .success(false)
                        .error("Address not found for user")
                        .build());
    }

    public AddressResponse createOrUpdateAddress(Integer userId, AddressRequest request) {
        try {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Address address = addressRepository.findByUserId(userId)
                    .orElse(Address.builder().user(user).build());

            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setZipCode(request.getZipCode());
            address.setCountry(request.getCountry());

            Address savedAddress = addressRepository.save(address);

            return AddressResponse.builder()
                    .id(savedAddress.getId())
                    .street(savedAddress.getStreet())
                    .city(savedAddress.getCity())
                    .state(savedAddress.getState())
                    .zipCode(savedAddress.getZipCode())
                    .country(savedAddress.getCountry())
                    .success(true)
                    .build();

        } catch (RuntimeException e) {
            return AddressResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    public void deleteAddress(Integer userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Address not found for user"));
        addressRepository.delete(address);
    }
}

