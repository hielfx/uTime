'use strict';

describe('Controller Tests', function() {

    describe('Need Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNeed, MockAppUser, MockNeededAbility, MockDisponibility;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNeed = jasmine.createSpy('MockNeed');
            MockAppUser = jasmine.createSpy('MockAppUser');
            MockNeededAbility = jasmine.createSpy('MockNeededAbility');
            MockDisponibility = jasmine.createSpy('MockDisponibility');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Need': MockNeed,
                'AppUser': MockAppUser,
                'NeededAbility': MockNeededAbility,
                'Disponibility': MockDisponibility
            };
            createController = function() {
                $injector.get('$controller')("NeedDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:needUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
