(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonDetailController', NaturalPersonDetailController);

    NaturalPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NaturalPerson', 'Gender', 'AppUser', 'Curriculum'];

    function NaturalPersonDetailController($scope, $rootScope, $stateParams, entity, NaturalPerson, Gender, AppUser, Curriculum) {
        var vm = this;
        vm.naturalPerson = entity;
        vm.load = function (id) {
            NaturalPerson.get({id: id}, function(result) {
                vm.naturalPerson = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:naturalPersonUpdate', function(event, result) {
            vm.naturalPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
