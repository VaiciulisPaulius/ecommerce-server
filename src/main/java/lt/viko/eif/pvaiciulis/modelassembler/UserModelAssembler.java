package lt.viko.eif.pvaiciulis.modelassembler;

import lt.viko.eif.pvaiciulis.controller.UserController;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A component that assembles a representation model of User.
 */
@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    /**
     * Converts a User object into an EntityModel containing the user data along with a self-link to retrieve the user by ID.
     *
     * @param user the user object to be converted into a model.
     * @return an EntityModel containing the user data and a self-link to retrieve the user by ID.
     */
    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId(), null)).withSelfRel());
    }
}
