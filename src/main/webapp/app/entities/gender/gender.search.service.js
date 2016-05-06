/**
 * Created by Daniel Sánchez on 04/05/2016.
 */
(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('GenderSearch', GenderSearch);
    GenderSearch.$inject = ['$resource'];

    function GenderSearch($resource) {
        return $resource('api/_search/genders', {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
