(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Administrator', Administrator);

    Administrator.$inject = ['$resource'];

    function Administrator ($resource) {
        var resourceUrl =  'api/administrators/:id';

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
