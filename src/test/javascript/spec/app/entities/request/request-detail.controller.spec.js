'use strict';

describe('Controller Tests', function() {

    describe('Request Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRequest, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRequest = jasmine.createSpy('MockRequest');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Request': MockRequest,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("RequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:requestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
