(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('LegalEntitySearch', LegalEntitySearch);

    LegalEntitySearch.$inject = ['$resource'];

    function LegalEntitySearch($resource) {
        var resourceUrl =  'api/_search/legal-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
