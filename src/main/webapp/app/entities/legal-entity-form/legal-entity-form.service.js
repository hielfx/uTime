(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('LegalEntityForm', LegalEntityForm);

    LegalEntityForm.$inject = ['$resource', 'DateUtils'];

    function LegalEntityForm($resource, DateUtils) {
        var resourceUrl = 'api/legal-entity-forms/:id';

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
