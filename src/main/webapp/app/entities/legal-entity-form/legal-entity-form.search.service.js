(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('LegalEntityFormSearch', LegalEntityFormSearch);

    LegalEntityFormSearch.$inject = ['$resource'];

    function LegalEntityFormSearch($resource) {
        var resourceUrl = 'api/_search/legal-entity-forms/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
