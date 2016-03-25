'use strict';

angular.module('volunteercrowdApp')
    .factory('LegalEntitySearch', function ($resource) {
        return $resource('api/_search/legalEntitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
