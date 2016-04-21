(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AppUserDetailController', AppUserDetailController);

    AppUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'AppUser', 'User', 'NaturalPerson', 'LegalEntity', 'Ability', 'Request', 'Need', 'Assessment', 'Payment', 'Incidence'];

    function AppUserDetailController($scope, $rootScope, $stateParams, DataUtils, entity, AppUser, User, NaturalPerson, LegalEntity, Ability, Request, Need, Assessment, Payment, Incidence) {
        var vm = this;
        vm.appUser = entity;
        vm.load = function (id) {
            AppUser.get({id: id}, function(result) {
                vm.appUser = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:appUserUpdate', function(event, result) {
            vm.appUser = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
