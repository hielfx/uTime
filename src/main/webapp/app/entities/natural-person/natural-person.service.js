(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('NaturalPerson', NaturalPerson);

    NaturalPerson.$inject = ['$resource', 'DateUtils'];

    function NaturalPerson ($resource, DateUtils) {
        var resourceUrl =  'api/natural-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
