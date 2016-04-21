(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('AppUser', AppUser);

    AppUser.$inject = ['$resource'];

    function AppUser ($resource) {
        var resourceUrl =  'api/app-users/:id';

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
