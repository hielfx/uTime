(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
