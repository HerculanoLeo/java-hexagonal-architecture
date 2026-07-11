package com.lodh8.starter.identity.clientapi.infra;

import com.herculanoleo.spring.me.models.enums.MapperEnum;
import com.lodh8.starter.identity.clientapi.domain.ClientApiRegister;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.keycloak.representations.idm.ClientRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientApiMapper {

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "name", source = "nome")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "alwaysDisplayInConsole", ignore = true)
    @Mapping(target = "surrogateAuthRequired", ignore = true)
    @Mapping(target = "rootUrl", ignore = true)
    @Mapping(target = "adminUrl", ignore = true)
    @Mapping(target = "baseUrl", ignore = true)
    @Mapping(target = "clientAuthenticatorType", ignore = true)
    @Mapping(target = "secret", ignore = true)
    @Mapping(target = "registrationAccessToken", ignore = true)
    @Mapping(target = "redirectUris", ignore = true)
    @Mapping(target = "webOrigins", ignore = true)
    @Mapping(target = "defaultRoles", ignore = true)
    @Mapping(target = "notBefore", ignore = true)
    @Mapping(target = "bearerOnly", ignore = true)
    @Mapping(target = "consentRequired", ignore = true)
    @Mapping(target = "standardFlowEnabled", ignore = true)
    @Mapping(target = "implicitFlowEnabled", ignore = true)
    @Mapping(target = "directAccessGrantsEnabled", ignore = true)
    @Mapping(target = "serviceAccountsEnabled", ignore = true)
    @Mapping(target = "authorizationServicesEnabled", ignore = true)
    @Mapping(target = "directGrantsOnly", ignore = true)
    @Mapping(target = "publicClient", ignore = true)
    @Mapping(target = "fullScopeAllowed", ignore = true)
    @Mapping(target = "protocol", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "authenticationFlowBindingOverrides", ignore = true)
    @Mapping(target = "nodeReRegistrationTimeout", ignore = true)
    @Mapping(target = "registeredNodes", ignore = true)
    @Mapping(target = "frontchannelLogout", ignore = true)
    @Mapping(target = "protocolMappers", ignore = true)
    @Mapping(target = "defaultClientScopes", ignore = true)
    @Mapping(target = "optionalClientScopes", ignore = true)
    @Mapping(target = "authorizationSettings", ignore = true)
    @Mapping(target = "access", ignore = true)
    @Mapping(target = "origin", ignore = true)
    ClientRepresentation representation(ClientApiRegister requestEntity);

    @Named("mapAttributesToTipo")
    default TipoAcesso mapAttributesToTipo(Map<String, List<String>> attributes) {
        var values = attributes.get(TipoAcesso.APPLICATION_TYPE_KEY);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return MapperEnum.fromValue(values.getFirst(), TipoAcesso.class);
    }


}
