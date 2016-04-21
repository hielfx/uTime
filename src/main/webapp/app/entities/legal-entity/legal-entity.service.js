(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('LegalEntity', LegalEntity);

    LegalEntity.$inject = ['$resource'];

    function LegalEntity ($resource) {
        var resourceUrl =  'api/legal-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
