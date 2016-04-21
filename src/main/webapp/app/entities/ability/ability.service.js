(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Ability', Ability);

    Ability.$inject = ['$resource'];

    function Ability ($resource) {
        var resourceUrl =  'api/abilities/:id';

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
