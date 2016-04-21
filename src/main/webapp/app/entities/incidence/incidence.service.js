(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Incidence', Incidence);

    Incidence.$inject = ['$resource', 'DateUtils'];

    function Incidence ($resource, DateUtils) {
        var resourceUrl =  'api/incidences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
