(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('PaymentSearch', PaymentSearch);

    PaymentSearch.$inject = ['$resource'];

    function PaymentSearch($resource) {
        var resourceUrl =  'api/_search/payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();