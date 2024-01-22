module Shared {
    exports dto.requests;
    exports dto.responses;
    exports lookupnames;
    exports dto.Controller;
    exports dto.Model;

    requires java.rmi;
    requires java.sql;
}