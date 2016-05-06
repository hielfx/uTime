(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('NaturalPersonForm', NaturalPersonForm);

    NaturalPersonForm.$inject = ['$resource', 'DateUtils'];

    function NaturalPersonForm($resource, DateUtils) {
        var resourceUrl = 'api/natural-person-forms/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
