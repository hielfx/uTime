(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('NaturalPersonFormSearch', NaturalPersonFormSearch);

    NaturalPersonFormSearch.$inject = ['$resource'];

    function NaturalPersonFormSearch($resource) {
        var resourceUrl = 'api/_search/natural-person-forms/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
