(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('NeededAbility', NeededAbility);

    NeededAbility.$inject = ['$resource'];

    function NeededAbility ($resource) {
        var resourceUrl =  'api/needed-abilities/:id';

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
